CREATE TABLE users (
                       user_id        NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                       email          VARCHAR2(100) NOT NULL UNIQUE,
                       password_hash  VARCHAR2(255) NOT NULL,
                       role           VARCHAR2(20) CHECK (role IN ('EMPLOYEE','MANAGER','ADMIN')),
                       is_active      NUMBER(1) DEFAULT 1,
                       last_login     TIMESTAMP,
                       created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
2
CREATE TABLE department (
                            department_id   NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                            department_name VARCHAR2(100) UNIQUE NOT NULL
);

CREATE TABLE employee (
                          emp_id             NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                          user_id            NUMBER NOT NULL UNIQUE,
                          first_name         VARCHAR2(100),
                          last_name          VARCHAR2(100),
                          phone              VARCHAR2(20),
                          address            CLOB,
                          emergency_contact  VARCHAR2(20),
                          dob                DATE,
                          joining_date       DATE,
                          department_id      NUMBER,
                          designation        VARCHAR2(100),
                          manager_id         NUMBER,
                          salary             NUMBER(10,2),
                          status             VARCHAR2(50),
                          CONSTRAINT fk_emp_user FOREIGN KEY (user_id) REFERENCES users(user_id),
                          CONSTRAINT fk_emp_dept FOREIGN KEY (department_id) REFERENCES department(department_id),
                          CONSTRAINT fk_emp_manager FOREIGN KEY (manager_id) REFERENCES employee(emp_id)
);

CREATE TABLE leave_type (
                            leave_type_id  NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                            leave_name     VARCHAR2(100),
                            max_per_year   NUMBER
);

CREATE TABLE leave_balance (
                               balance_id     NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                               emp_id         NUMBER NOT NULL,
                               leave_type_id  NUMBER NOT NULL,
                               balance_days   NUMBER,
                               CONSTRAINT uq_leave_balance UNIQUE (emp_id, leave_type_id),
                               CONSTRAINT fk_lb_emp FOREIGN KEY (emp_id) REFERENCES employee(emp_id),
                               CONSTRAINT fk_lb_leave_type FOREIGN KEY (leave_type_id) REFERENCES leave_type(leave_type_id)
);

CREATE TABLE leave_application (
                                   leave_id        NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                   emp_id          NUMBER NOT NULL,
                                   leave_type_id   NUMBER NOT NULL,
                                   start_date      DATE,
                                   end_date        DATE,
                                   reason          CLOB,
                                   status          VARCHAR2(50),
                                   applied_on      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                   approved_by     NUMBER,
                                   manager_comment CLOB,
                                   CONSTRAINT fk_la_emp FOREIGN KEY (emp_id) REFERENCES employee(emp_id),
                                   CONSTRAINT fk_la_leave_type FOREIGN KEY (leave_type_id) REFERENCES leave_type(leave_type_id),
                                   CONSTRAINT fk_la_approved_by FOREIGN KEY (approved_by) REFERENCES employee(emp_id)
);

CREATE TABLE holiday_calendar (
                                  holiday_id    NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                  holiday_date  DATE,
                                  description   VARCHAR2(255)
);

CREATE TABLE performance_review (
                                    review_id        NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                    emp_id           NUMBER NOT NULL,
                                    year             NUMBER,
                                    achievements     CLOB,
                                    improvements     CLOB,
                                    self_rating      NUMBER,
                                    manager_rating   NUMBER,
                                    manager_feedback CLOB,
                                    status           VARCHAR2(50),
                                    CONSTRAINT uq_perf_review UNIQUE (emp_id, year),
                                    CONSTRAINT fk_pr_emp FOREIGN KEY (emp_id) REFERENCES employee(emp_id)
);

CREATE TABLE goals (
                       goal_id        NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                       emp_id         NUMBER NOT NULL,
                       goal_desc      CLOB,
                       deadline       DATE,
                       priority       VARCHAR2(20),
                       success_metric VARCHAR2(255),
                       progress       NUMBER,
                       status         VARCHAR2(50),
                       CONSTRAINT fk_goal_emp FOREIGN KEY (emp_id) REFERENCES employee(emp_id)
);

CREATE TABLE notifications (
                               notification_id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                               user_id         NUMBER NOT NULL,
                               message         CLOB,
                               type            VARCHAR2(50),
                               is_read         NUMBER(1) DEFAULT 0,
                               created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               CONSTRAINT fk_notif_user FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE announcements (
                               announcement_id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                               title           VARCHAR2(200),
                               message         CLOB,
                               created_by      NUMBER,
                               created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               CONSTRAINT fk_announce_user FOREIGN KEY (created_by) REFERENCES users(user_id)
);

CREATE TABLE events (
                        event_id    NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                        emp_id      NUMBER NOT NULL,
                        event_type  VARCHAR2(100),
                        event_date  DATE,
                        CONSTRAINT fk_event_emp FOREIGN KEY (emp_id) REFERENCES employee(emp_id)
);
