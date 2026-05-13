-- Dev seed: runs only with H2 on startup
-- Users are created via /api/auth/register in prod

INSERT INTO users (id, name, email, password, role, created_at)
VALUES
  (1, 'Ayşe Yılmaz', 'ayse@bonvo.ai',
   '$2a$10$7EqJtq98hPqEX7fNZaFWoO0kLkLhcXHu8eXy1e6z3BjlH1YrPAMoe', -- 'password123'
   'USER', NOW())
ON CONFLICT DO NOTHING;
