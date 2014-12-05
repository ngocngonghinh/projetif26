<?php
$MYSQL_DB='b7_15623215_projet';
$MYSQL_HOST='sql200.byethost7.com';
$MYSQL_USER='b7_15623215';
$MYSQL_PWD='utt123456';
$MYSQL_DSN="mysql:dbname=" .$MYSQL_DB .";host=". $MYSQL_HOST;

return array
(
	'dsn' => $MYSQL_DSN,
	'username' => $MYSQL_USER,
	'password' => $MYSQL_PWD,
	'options' => array
	(
		PDO::MYSQL_ATTR_INIT_COMMAND => "SET NAMES 'utf8'"
	)
);
