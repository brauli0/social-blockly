-- ----------------------------------------------------------------------------
-- Put here INSERT statements for inserting data required by the application
-- in the "socialblockly" database.
-------------------------------------------------------------------------------

-- USERS

INSERT INTO User(userName, password, firstName, lastName, email, role)
VALUES ("example", "$2a$10$qSDD./JuX./cwRJ/bFbN0.rU1tvhKVuS/q.GnUfmbXeZpWfAfMDSW", "Example", "Example", "example@example", 0);

INSERT INTO User(userName, password, firstName, lastName, email, role)
VALUES ("braulio", "$2a$10$qSDD./JuX./cwRJ/bFbN0.rU1tvhKVuS/q.GnUfmbXeZpWfAfMDSW", "Braulio", "Méndez", "braulio.mendez@udc.es", 1);

INSERT INTO User(userName, password, firstName, lastName, email, role)
VALUES ("john", "$2a$10$qSDD./JuX./cwRJ/bFbN0.rU1tvhKVuS/q.GnUfmbXeZpWfAfMDSW", "John", "Lewis", "john.lewis@udc.es", 0);

INSERT INTO User(userName, password, firstName, lastName, email, role)
VALUES ("lucas", "$2a$10$qSDD./JuX./cwRJ/bFbN0.rU1tvhKVuS/q.GnUfmbXeZpWfAfMDSW", "Lucas", "Fuentes", "lucas.fuentes@udc.es", 0);

INSERT INTO User(userName, password, firstName, lastName, email, role)
VALUES ("daniel", "$2a$10$qSDD./JuX./cwRJ/bFbN0.rU1tvhKVuS/q.GnUfmbXeZpWfAfMDSW", "Daniel", "Vázquez", "daniel.vazquez@udc.es", 1);

INSERT INTO User(userName, password, firstName, lastName, email, role)
VALUES ("miguel", "$2a$10$qSDD./JuX./cwRJ/bFbN0.rU1tvhKVuS/q.GnUfmbXeZpWfAfMDSW", "Miguel", "Gallardo", "miguel.gallardo@udc.es", 0);

INSERT INTO User(userName, password, firstName, lastName, email, role)
VALUES ("michel", "$2a$10$qSDD./JuX./cwRJ/bFbN0.rU1tvhKVuS/q.GnUfmbXeZpWfAfMDSW", "Michel", "Roca", "michel.roca@udc.es", 0);

INSERT INTO User(userName, password, firstName, lastName, email, role)
VALUES ("mateo", "$2a$10$qSDD./JuX./cwRJ/bFbN0.rU1tvhKVuS/q.GnUfmbXeZpWfAfMDSW", "Mateo", "Cano", "mateo.cano@udc.es", 0);

INSERT INTO User(userName, password, firstName, lastName, email, role)
VALUES ("arturo", "$2a$10$qSDD./JuX./cwRJ/bFbN0.rU1tvhKVuS/q.GnUfmbXeZpWfAfMDSW", "Arturo", "Campos", "arturo.campos@udc.es", 1);

-- PROGRAMS

INSERT INTO Program(userId, programName, programDesc, code)
VALUES (1, "Example program", "Program to show in the main page of the web app.", "<xml xmlns=\"http://www.w3.org/1999/xhtml\"><block type=\"variables_set\" id=\"EDbEtQ@5:pEb)@-8=fO`\" x=\"27\" y=\"61\"><field name=\"VAR\">x</field><value name=\"VALUE\"><block type=\"math_number\" id=\"SKldenGqb(a=DzZbTZe_\"><field name=\"NUM\">0</field></block></value><next><block type=\"variables_set\" id=\"TNRs%}4hGEPPGY8zhOM#\"><field name=\"VAR\">counter</field><value name=\"VALUE\"><block type=\"math_number\" id=\"YB*/k@(AY)cSr^91@{F5\"><field name=\"NUM\">0</field></block></value><next><block type=\"controls_whileUntil\" id=\"@f4_4F`25lQN0rDN8F.e\"><field name=\"MODE\">WHILE</field><value name=\"BOOL\"><block type=\"logic_compare\" id=\"MjVH3,{d=1Y,(~z=u,}i\"><field name=\"OP\">NEQ</field><value name=\"A\"><block type=\"variables_get\" id=\"7jrUNbL2`OsOJQKaAAUx\"><field name=\"VAR\">x</field></block></value><value name=\"B\"><block type=\"math_number\" id=\"v,*O6IqKkYjo[j:7b%gv\"><field name=\"NUM\">1</field></block></value></block></value><statement name=\"DO\"><block type=\"variables_set\" id=\"jUU^Sg4Gu)MAysfv.zI*\"><field name=\"VAR\">x</field><value name=\"VALUE\"><block type=\"math_random_int\" id=\"_b4,d1:7AD6nSqWH./w,\"><value name=\"FROM\"><block type=\"math_number\" id=\"%n@?ESlNVYYiFx,(P+*`\"><field name=\"NUM\">1</field></block></value><value name=\"TO\"><block type=\"math_number\" id=\"A9Q=:|If4[;GWatFGWMC\"><field name=\"NUM\">100</field></block></value></block></value><next><block type=\"variables_set\" id=\"5?Y2,Oirjk!;n}+zFeJo\"><field name=\"VAR\">counter</field><value name=\"VALUE\"><block type=\"math_arithmetic\" id=\"+gVP`IF^qP_8wp]f42g4\"><field name=\"OP\">ADD</field><value name=\"A\"><block type=\"variables_get\" id=\"!LM%A8T_nP;_B3=?::oh\"><field name=\"VAR\">counter</field></block></value><value name=\"B\"><block type=\"math_number\" id=\"Qh_G-+k;BIRY}eHi/uf~\"><field name=\"NUM\">1</field></block></value></block></value></block></next></block></statement><next><block type=\"text_print\" id=\"S3U-N-UKF~Y3t!~D+LZm\"><value name=\"TEXT\"><shadow type=\"text\" id=\";9O%-Gixkg{^iyhDF/N0\"><field name=\"TEXT\"></field></shadow><block type=\"text_join\" id=\"}TS^zU/nl1N_KQ.|9(Z5\"><mutation items=\"2\"></mutation><value name=\"ADD0\"><block type=\"text\" id=\"2[~%4DHkVxMM)Q,,*d;i\"><field name=\"TEXT\">Nº de iteraciones: </field></block></value><value name=\"ADD1\"><block type=\"variables_get\" id=\"vlnK!+[BER#2{yt1#^x,\"><field name=\"VAR\">counter</field></block></value></block></value></block></next></block></next></block></next></block></xml>");

INSERT INTO Program(userId, programName, programDesc)
VALUES (2, "This is the program's name", "This is the description of the program where we can explain its purpose or something else");

INSERT INTO Program(userId, programName, programDesc, privateProgram)
VALUES (2, "Hello world 2", "Description 2...", 1);

INSERT INTO Program(userId, programName, programDesc, privateProgram)
VALUES (2, "Hello world 3", "Description 3...", 1);

INSERT INTO Program(userId, programName, programDesc)
VALUES (4, "Hello world 1", "Description 1...");

INSERT INTO Program(userId, programName, programDesc)
VALUES (4, "Hello world 2", "Description 2...");

INSERT INTO Program(userId, programName, programDesc, privateProgram)
VALUES (4, "Hello world 3", "Description 3...", 1);

-- GROUPS

INSERT INTO UsersGroup(groupName)
VALUES ("Informática 1");

INSERT INTO UsersGroup(groupName)
VALUES ("Informática 2");

INSERT INTO UsersGroup(groupName)
VALUES ("Tecnoloxía");

INSERT INTO UsersGroup(groupName)
VALUES ("Clase de programación");

-- Group MEMBERS

INSERT INTO UsersGroupRel(userId, groupId, isAdmin)
VALUES (2, 1, TRUE);

INSERT INTO UsersGroupRel(userId, groupId, isAdmin)
VALUES (3, 1, FALSE);

INSERT INTO UsersGroupRel(userId, groupId, isAdmin)
VALUES (4, 1, FALSE);

INSERT INTO UsersGroupRel(userId, groupId, isAdmin)
VALUES (5, 2, TRUE);

INSERT INTO UsersGroupRel(userId, groupId, isAdmin)
VALUES (6, 2, FALSE);

INSERT INTO UsersGroupRel(userId, groupId, isAdmin)
VALUES (7, 2, FALSE);

INSERT INTO UsersGroupRel(userId, groupId, isAdmin)
VALUES (7, 3, FALSE);

INSERT INTO UsersGroupRel(userId, groupId, isAdmin)
VALUES (8, 3, FALSE);

INSERT INTO UsersGroupRel(userId, groupId, isAdmin)
VALUES (9, 3, TRUE);

INSERT INTO UsersGroupRel(userId, groupId, isAdmin)
VALUES (2, 4, TRUE);

INSERT INTO UsersGroupRel(userId, groupId, isAdmin)
VALUES (6, 4, FALSE);

INSERT INTO UsersGroupRel(userId, groupId, isAdmin)
VALUES (8, 4, FALSE);

-- Chat

INSERT INTO ChatMessage(groupId, userId, messageText)
VALUES (1, 2, 'Hello!');

INSERT INTO ChatMessage(groupId, userId, messageText)
VALUES (1, 3, 'Hi, how are you?');

INSERT INTO ChatMessage(groupId, userId, messageText)
VALUES (1, 2, 'Fine, thanks!');

--COMMENT

INSERT INTO Comment(userId, programId, commentText)
VALUES(2, 2, 'First comment');

INSERT INTO Comment(userId, programId, commentText)
VALUES(4, 2, 'Second comment');

INSERT INTO Comment(userId, programId, commentText)
VALUES(2, 2, 'Third comment');

INSERT INTO Comment(userId, programId, commentOrigId, commentText)
VALUES(4, 2, 2, 'Reply');

INSERT INTO Comment(userId, programId, commentOrigId, commentText)
VALUES(2, 2, 2, 'Other reply');

-- RATINGS

INSERT INTO Rating(userId, programId, rating)
VALUES(2, 2, 7);

INSERT INTO Rating(userId, programId, rating)
VALUES(3, 2, 9);

INSERT INTO Rating(userId, programId, rating)
VALUES(4, 2, 6);

-- SHARED

INSERT INTO Shared(userId, programId)
VALUES (3, 4);

INSERT INTO Shared(userId, programId)
VALUES (4, 4);

INSERT INTO Shared(userId, programId)
VALUES (3, 7);

-- EXERCISES

INSERT INTO Exercise(statementText, groupId, userId, expirationDate, blocks)
VALUES ('This is the statement of the first exercise...', 1, 2, '2020-07-25 13:59:00', 'YYYYYYYY');

INSERT INTO Exercise(statementText, groupId, userId, expirationDate, blocks)
VALUES ('Other exercise...', 1, 2, '2020-07-25 15:59:00', 'YYNYYYYN');

-- Solution to exercise
INSERT INTO Program(userId, exerciseId, programName, programDesc, privateProgram)
VALUES (2, 1, "First solution", "This is a solution for this exercise...", 1);

INSERT INTO Program(userId, exerciseId, programName, programDesc, privateProgram)
VALUES (2, 1, "Second solution", "This is other solution for this exercise...", 1);