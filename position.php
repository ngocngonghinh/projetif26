<?php
require_once('database/db.php');
require_once('model/user.php');
$parameters = array
(
	':token' => null
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
$user = $db->find('User', 'user', 'token = :token', array(":token"=>$parameters[":token"]));
if($user !== false)
{
	$user->id = (int) $user->id;
        $user->latitude=$parameters[":latitude"];
        $user->longtitude=$parameters[":longtitude"];
        
	$db->update($user, 'user', 'id = :id', array(':id' => $user->id));
}
// echo json_encode($json, JSON_PRETTY_PRINT);            5.4 required!!
$users = $db->search('User', 'user', 'longtitude != 0 AND latitude != 0', array());
foreach($users as $user) {
        unset($user->email);
        unset($user->password);
}
		$json = array(
			'error' => false,
			'users' => $users
		);
echo json_encode($json);

