<?php

require_once('../database/db.php');
require_once('../model/user.php');
require_once('../model/token.php');
$parameters = array
    (
    ':token' => null
);
foreach ($_POST as $key => $value) {
    $parameters[":$key"] = $value;
}
$json = array(
    'error' => true
);
$config = require_once('config.php');
$db = new DB($config['dsn'], $config['username'], $config['password'], $config['options']);

function getUserFromToken() {
    global $json, $db, $parameters;
    if (isset($_POST["token"])) {
        if (isexpired($_POST["token"])) {
            $json["error_msg"] = "Token is expired";
        } else {
            $user = $db->find('User', 'user', 'token = :token', array(':token' => $parameters[":token"]));
            return $user;
        }
    }
    return false;
}
