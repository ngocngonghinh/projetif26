<?php
require_once('database/db.php');
require_once('model/user.php');

$parameters = array
(
	':email' => null,
	':password' => null
);
foreach($_GET as $key => $value)
{
	$parameters[":$key"] = $value;
}

$json = array(
	'error' => true
);

$config = require_once('config.php');
$db = new DB($config['dsn'], $config['username'], $config['password'], $config['options']);
$user = $db->find('User', 'user', 'email = :email AND password = :password', $parameters);
if($user !== false)
{
	$token = md5(time() . $user->email . $user->password);
	$user->token = $token;
	if($db->update($user, 'user', 'id = :id', array(':id' => $user->id)))
	{
		$json = array(
			'error' => false,
			'token' => $token
		);
	}
}
// echo json_encode($json, JSON_PRETTY_PRINT);            5.4 required!!
echo json_encode($json);