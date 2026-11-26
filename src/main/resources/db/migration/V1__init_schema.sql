CREATE TABLE users (
                       id            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                       username      VARCHAR(100) NOT NULL UNIQUE,
                       email         VARCHAR(255) NOT NULL UNIQUE,
                       password_hash VARCHAR(255) NOT NULL,
                       system_role   VARCHAR(30)  NOT NULL DEFAULT 'USER'
                           CHECK (system_role IN ('ADMIN', 'USER')),
                       created_at    TIMESTAMP    NOT NULL DEFAULT now()
);

CREATE TABLE skill (
                       id   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                       name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE user_skill (
                            user_id  BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                            skill_id BIGINT NOT NULL REFERENCES skill(id) ON DELETE CASCADE,
                            level    INTEGER CHECK (level BETWEEN 1 AND 5),
                            PRIMARY KEY (user_id, skill_id)
);

CREATE TABLE project (
                         id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                         name        VARCHAR(200) NOT NULL,
                         description TEXT,
                         owner_id    BIGINT NOT NULL REFERENCES users(id),
                         created_at  TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE project_member (
                                project_id BIGINT NOT NULL REFERENCES project(id) ON DELETE CASCADE,
                                user_id    BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                                role       VARCHAR(30) NOT NULL
                                    CHECK (role IN ('PROJECT_MANAGER', 'DEVELOPER', 'QA', 'DEVOPS', 'ANALYST')),
                                PRIMARY KEY (project_id, user_id)
);

CREATE TABLE sprint (
                        id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                        project_id BIGINT NOT NULL REFERENCES project(id) ON DELETE CASCADE,
                        name       VARCHAR(200) NOT NULL,
                        start_date DATE,
                        end_date   DATE,
                        status     VARCHAR(30) NOT NULL DEFAULT 'PLANNED'
                            CHECK (status IN ('PLANNED', 'ACTIVE', 'COMPLETED')),
                        CHECK (end_date IS NULL OR start_date IS NULL OR end_date >= start_date)
);

CREATE TABLE task (
                      id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                      project_id   BIGINT NOT NULL REFERENCES project(id) ON DELETE CASCADE,
                      sprint_id    BIGINT REFERENCES sprint(id) ON DELETE SET NULL,
                      parent_id    BIGINT REFERENCES task(id) ON DELETE SET NULL,
                      status       VARCHAR(30) NOT NULL DEFAULT 'TODO',
                      name         VARCHAR(255) NOT NULL,
                      description  TEXT,
                      priority     INTEGER NOT NULL DEFAULT 3,
                      assignee_id  BIGINT REFERENCES users(id) ON DELETE SET NULL,
                      reporter_id  BIGINT NOT NULL REFERENCES users(id),
                      estimate     INTEGER,
                      requirement  VARCHAR(255),
                      created_at   TIMESTAMP NOT NULL DEFAULT now(),
                      updated_at   TIMESTAMP NOT NULL DEFAULT now(),
                      completed_at TIMESTAMP,
                      CHECK (parent_id IS NULL OR parent_id <> id)
);

CREATE TABLE task_comment (
                              id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                              task_id    BIGINT NOT NULL REFERENCES task(id) ON DELETE CASCADE,
                              user_id    BIGINT NOT NULL REFERENCES users(id),
                              text       TEXT NOT NULL,
                              created_at TIMESTAMP NOT NULL DEFAULT now(),
                              updated_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE task_history (
                              id            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                              task_id       BIGINT NOT NULL REFERENCES task(id) ON DELETE CASCADE,
                              user_id       BIGINT NOT NULL REFERENCES users(id),
                              field_changed VARCHAR(100) NOT NULL,
                              old_value     TEXT,
                              new_value     TEXT,
                              updated_at    TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE recommendation (
                                id                BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                task_id           BIGINT NOT NULL REFERENCES task(id) ON DELETE CASCADE,
                                suggested_user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                                reason            TEXT,
                                accepted          BOOLEAN
);

CREATE INDEX idx_task_project  ON task(project_id);
CREATE INDEX idx_task_sprint   ON task(sprint_id);
CREATE INDEX idx_task_parent   ON task(parent_id);
CREATE INDEX idx_task_assignee ON task(assignee_id);
CREATE INDEX idx_comment_task  ON task_comment(task_id);
CREATE INDEX idx_history_task  ON task_history(task_id);