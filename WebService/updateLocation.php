<?php

require_once('database/db.php');
require_once('model/user.php');
require_once('model/token.php');
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
        $user->setLatitude($parameters[":latitude"]);
        $user->setLongtitude($parameters[":longtitude"]);
        if($db->update($user, 'user', 'id = :id', array(':id' => $user->getId()))){
        $json = array(
            'error' => false,
            'user' => $user->simply()
        );
        }
    }
}
echo json_encode($json);

