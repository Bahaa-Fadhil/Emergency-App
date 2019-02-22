SELECT c.name, c.phone AS 'Contact phone', u.phone AS 'User phone', diagnose
FROM contact c
	JOIN user u ON u.idUser = c.idUser
WHERE c.idUser = 2;