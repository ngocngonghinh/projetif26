<?php
require_once('database/db.php');
require_once('model/friend.php');
require_once('model/token.php');
require_once('model/user.php');
$json = array(
	'error' => true
);
$parameters = array
(
	':token' => null
);
foreach($_GET as $key => $value)
{
	$parameters[":$key"] = $value;
}
if (isexpired($parameters[":token"])){ 
    $json = array(
	'error' => true,
        "error_msg"=> "Token is expired"
    );           
}
else
{
$config = require_once('config.php');
$db= new DB($config['dsn'], $config['username'], $config['password'], $config['options']);
$user = $db->find('User', 'user', 'token = :token', array(":token"=>$parameters[":token"]));
if($user !== false)
{
        $friend=new Friend(); 
        $friend->update($parameters[":id"],$user->getId(),1);
	$db->update($friend, 'friends', 'id_user1 = :id_user1 AND id_user2 = :id_user2', array());
	$json = array(
		'error' => false,
		'friends' => $friend->toDB()
	);            
}
}
echo json_encode($json);


