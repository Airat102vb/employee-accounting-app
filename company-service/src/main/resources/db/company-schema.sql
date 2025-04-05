CREATE TABLE IF NOT EXISTS companies (
  id SERIAL PRIMARY KEY,
  company_name VARCHAR(100) NOT NULL,
  budget NUMERIC(15,2)
);

CREATE TABLE IF NOT EXISTS company_employees (
  company_id INTEGER REFERENCES companies(id),
  employee_id INTEGER,
  PRIMARY KEY (company_id, employee_id)
);