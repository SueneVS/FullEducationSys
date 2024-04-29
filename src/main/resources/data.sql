INSERT INTO papel (id_papel, nome)
VALUES (1, 'ADM'),
       (2, 'PEDAGOGICO'),
       (3, 'RECRUITER'),
       (4, 'PROFESSOR'),
       (5, 'ALUNO')
on conflict (id_papel) do nothing;


INSERT INTO usuario (login, senha, id_papel)
Values ('user1', '$2a$10$1dTOY9VqfKXpPgksomhMXu6yz0X6KbbPP6Hod0AVFhr5mXKWNTVhS', 1)
on conflict (login) do nothing;