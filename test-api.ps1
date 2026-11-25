
$BASE = "http://localhost:8080"

$Passed = 0
$Failed = 0

# Уникальный пользователь для каждого запуска
$Suffix = Get-Date -Format "yyyyMMddHHmmss"
$Username = "test_user_$Suffix"
$Email = "test_$Suffix@example.com"
$Password = "TestPassword123!"

$AccessToken = $null
$RefreshToken = $null

Write-Host "`n========== API TESTS ==========" -ForegroundColor Cyan

function Send-Request {
    param(
        [string]$Method = "GET",
        [string]$Path,
        [string]$Body = "",
        [string]$Token = ""
    )

    $ResponseFile = [System.IO.Path]::GetTempFileName()
    $BodyFile = $null

    try {
        $CurlArgs = @(
            "-sS"
            "-o", $ResponseFile
            "-w", "%{http_code}"
            "-X", $Method
            "$BASE$Path"
        )

        if ($Body -ne "") {
            $BodyFile = [System.IO.Path]::GetTempFileName()
            # UTF-8 без BOM, чтобы JSON не портился
            [System.IO.File]::WriteAllText($BodyFile, $Body, (New-Object System.Text.UTF8Encoding($false)))

            $CurlArgs += @(
                "-H", "Content-Type: application/json"
                "--data-binary", "@$BodyFile"
            )
        }

        if ($Token -ne "") {
            $CurlArgs += @(
                "-H", "Authorization: Bearer $Token"
            )
        }

        $StatusText = & curl.exe @CurlArgs
        $CurlExitCode = $LASTEXITCODE

        $ResponseBody = Get-Content -Path $ResponseFile -Raw

        if ($CurlExitCode -ne 0) {
            throw "curl.exe завершился с кодом $CurlExitCode"
        }

        return [PSCustomObject]@{
            Status = [int]$StatusText
            Body   = $ResponseBody
        }
    }
    finally {
        Remove-Item $ResponseFile -Force -ErrorAction SilentlyContinue
        if ($BodyFile) { Remove-Item $BodyFile -Force -ErrorAction SilentlyContinue }
    }
}

# Проверка результата
function Check {
    param(
        [string]$Name,
        [object]$Response,
        [int[]]$Expected,
        [string]$Extra = ""
    )

    if ($null -eq $Response) {
        Write-Host "[FAIL] $Name $Extra" -ForegroundColor Red
        $script:Failed++
        return
    }

    if ($Expected -contains $Response.Status) {
        Write-Host "[PASS] $Name (HTTP $($Response.Status))" -ForegroundColor Green
        $script:Passed++
    }
    else {
        Write-Host "[FAIL] $Name (HTTP $($Response.Status), expected: $($Expected -join ', '))" -ForegroundColor Red
        Write-Host "       Response: $($Response.Body)"
        $script:Failed++
    }
}

# Безопасно извлекаем JSON
function Get-Json {
    param([string]$Body)

    try {
        return $Body | ConvertFrom-Json
    }
    catch {
        return $null
    }
}

# 1. Registration
$RegisterBody = @{
    username = $Username
    email    = $Email
    password = $Password
} | ConvertTo-Json -Compress

$Register = Send-Request -Method "POST" -Path "/api/auth/register" -Body $RegisterBody
Check "Registration" $Register @(200, 201)

# Получаем токены, если регистрация их вернула
if ($Register.Status -in @(200, 201)) {
    $RegisterJson = Get-Json $Register.Body

    if ($null -ne $RegisterJson) {
        $AccessToken = $RegisterJson.accessToken
        $RefreshToken = $RegisterJson.refreshToken
    }
}

# 2. Duplicate registration
$Duplicate = Send-Request -Method "POST" -Path "/api/auth/register" -Body $RegisterBody
Check "Duplicate registration rejected" $Duplicate @(400, 409)

# 3. Invalid email
$InvalidEmailBody = @{
    username = "invalid_$Suffix"
    email    = "not-an-email"
    password = $Password
} | ConvertTo-Json -Compress

$InvalidEmail = Send-Request -Method "POST" -Path "/api/auth/register" -Body $InvalidEmailBody
Check "Invalid email rejected" $InvalidEmail @(400)

# 4. Empty password
$EmptyPasswordBody = @{
    username = "empty_$Suffix"
    email    = "empty_$Suffix@example.com"
    password = ""
} | ConvertTo-Json -Compress

$EmptyPassword = Send-Request -Method "POST" -Path "/api/auth/register" -Body $EmptyPasswordBody
Check "Empty password rejected" $EmptyPassword @(400)

# 5. Login with correct password
$LoginBody = @{
    username = $Username
    password = $Password
} | ConvertTo-Json -Compress

$Login = Send-Request -Method "POST" -Path "/api/auth/login" -Body $LoginBody
Check "Login with correct password" $Login @(200)

if ($Login.Status -eq 200) {
    $LoginJson = Get-Json $Login.Body

    if ($null -ne $LoginJson) {
        $AccessToken = $LoginJson.accessToken
        $RefreshToken = $LoginJson.refreshToken
    }
}

# 6. Login with wrong password
$WrongPasswordBody = @{
    username = $Username
    password = "WrongPassword123!"
} | ConvertTo-Json -Compress

$WrongPassword = Send-Request -Method "POST" -Path "/api/auth/login" -Body $WrongPasswordBody
Check "Login with wrong password rejected" $WrongPassword @(400, 401)

# 7. Unknown user login
$UnknownUserBody = @{
    username = "unknown_$Suffix"
    password = $Password
} | ConvertTo-Json -Compress

$UnknownUser = Send-Request -Method "POST" -Path "/api/auth/login" -Body $UnknownUserBody
Check "Unknown user login rejected" $UnknownUser @(400, 401)

# 8. Protected endpoint without JWT
$NoToken = Send-Request -Method "GET" -Path "/api/security-test"
Check "Protected endpoint without JWT" $NoToken @(401)

# 9. Invalid JWT
$InvalidToken = Send-Request -Method "GET" -Path "/api/security-test" -Token "invalid.jwt.token"
Check "Invalid JWT rejected" $InvalidToken @(401)

# 10. Valid JWT
if ($AccessToken) {
    $ValidToken = Send-Request -Method "GET" -Path "/api/security-test" -Token $AccessToken

    # 404 допустим, если тестового endpoint пока нет:
    # это означает, что запрос прошёл Security и дошёл до маршрутизации.
    Check "Valid JWT passes authentication" $ValidToken @(200, 404)
}
else {
    Write-Host "[FAIL] Valid JWT passes authentication (No access token)" -ForegroundColor Red
    $Failed++
}

# 11. Refresh token
if ($RefreshToken) {
    $RefreshBody = @{
        refreshToken = $RefreshToken
    } | ConvertTo-Json -Compress

    $Refresh = Send-Request -Method "POST" -Path "/api/auth/refresh" -Body $RefreshBody
    Check "Refresh token works" $Refresh @(200)
}
else {
    Write-Host "[FAIL] Refresh token works (No refresh token)" -ForegroundColor Red
    $Failed++
}

# 12. Invalid refresh token
$InvalidRefreshBody = @{
    refreshToken = "invalid.refresh.token"
} | ConvertTo-Json -Compress

$InvalidRefresh = Send-Request -Method "POST" -Path "/api/auth/refresh" -Body $InvalidRefreshBody
Check "Invalid refresh token rejected" $InvalidRefresh @(400, 401)

# 13. USER cannot access ADMIN endpoint
if ($AccessToken) {
    $AdminRequest = Send-Request -Method "GET" -Path "/api/admin/security-test" -Token $AccessToken
    Check "USER cannot access ADMIN endpoint" $AdminRequest @(403)
}
else {
    Write-Host "[FAIL] USER cannot access ADMIN endpoint (No access token)" -ForegroundColor Red
    $Failed++
}

# Results
Write-Host "`n========== RESULTS ==========" -ForegroundColor Cyan
Write-Host "Passed: $Passed" -ForegroundColor Green
Write-Host "Failed: $Failed" -ForegroundColor Red

if ($Failed -eq 0) {
    Write-Host "`nALL TESTS PASSED" -ForegroundColor Green
}
else {
    Write-Host "`nSOME TESTS FAILED" -ForegroundColor Red
}

$BASE = "http://localhost:8080"

function Call-Api {
    param(
        [string]$Method = "GET",
        [string]$Path,
        [string]$Body = "",
        [string]$Token = ""
    )
    $RespFile = [System.IO.Path]::GetTempFileName()
    $BodyFile = $null
    try {
        $a = @("-sS", "-o", $RespFile, "-w", "%{http_code}", "-X", $Method, "$BASE$Path")
        if ($Body -ne "") {
            $BodyFile = [System.IO.Path]::GetTempFileName()
            [System.IO.File]::WriteAllText($BodyFile, $Body, (New-Object System.Text.UTF8Encoding($false)))
            $a += @("-H", "Content-Type: application/json", "--data-binary", "@$BodyFile")
        }
        if ($Token -ne "") { $a += @("-H", "Authorization: Bearer $Token") }

        $status = & curl.exe @a
        $text = Get-Content $RespFile -Raw
        Write-Host "HTTP $status" -ForegroundColor Yellow
        Write-Host $text
    }
    finally {
        Remove-Item $RespFile -Force -ErrorAction SilentlyContinue
        if ($BodyFile) { Remove-Item $BodyFile -Force -ErrorAction SilentlyContinue }
    }
}