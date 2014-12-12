<?php

require_once('database/db.php');
require_once('model/user.php');

$json = array(
    'error' => true
);
$parameters = array
    (
    ':token' => null,
);
foreach ($_GET as $key => $value) {
    $parameters[":$key"] = $value;
}

$config = require_once('config.php');
$db = new DB($config['dsn'], $config['username'], $config['password'], $config['options']);
$user = $db->find('User', 'user', 'token = :token', array(":token" => $parameters[":token"]));
if ($user !== false) {
    $user->setToken(0);
    if ($db->update($user, 'user', 'id = :id', array(':id' => $user->getId()))) {
        $json = array(
            'error' => false
        );
    }
}
// echo json_encode($json, JSON_PRETTY_PRINT);            5.4 required!!
echo json_encode($json);
