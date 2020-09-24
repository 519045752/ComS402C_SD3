	DROP DATABASE IF EXISTS ar_project;
	CREATE DATABASE ar_project; 
	USE ar_project;
    
	CREATE TABLE `user`(
		`uid` SERIAL, 
        `username` varchar(30) NOT NULL, 
		`password` varchar(30) NOT NULL, 
		`category` varchar(30), 
		PRIMARY KEY(uid)
	);


INSERT INTO `user` VALUES 
(null,'admin','admin','admin'), 
(null,'Simanta','123321','landlord'),
(null,'Kenny','123321','tenant'),
(null,'Andrew','123321','tenant'),
(null,'Cole','123321','tenant'),
(null,'Jay','123321','tenant'),
(null,'Lim','123321','tenant')
;
SELECT * FROM user;
-- SELECT uid,username,category FROM user WHERE username='admin' AND password = 'admin';