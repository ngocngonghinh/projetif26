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
foreach ($_GET as $key => $value) {
    $parameters[":$key"] = $value;
}
if (isexpired($parameters[":token"])) {
    $json = array(
        'error' => true,
        "error_msg" => "Token is expired"
    );
} else {
    $config = require_once('config.php');
    $db = new DB($config['dsn'], $config['username'], $config['password'], $config['options']);
    $user = $db->find('User', 'user', 'token = :token', array(":token" => $parameters[":token"]));
    if ($user !== false) {
        if ($db->delete('friends', 'id_user1 = :id_user1 AND id_user2 = :id_user2', array("id_user1" => $user->getId(), "id_user2" => $parameters[":id"]))) {
            $json = array(
                'error' => false,
            );
        }
    }
}
echo json_encode($json);




