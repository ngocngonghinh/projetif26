<?php

require_once('database/db.php');
require_once('model/user.php');
require_once('model/token.php');
$json = array(
    'error' => true
);
if (isset($_GET["token"])) {
    if (isexpired($_GET["token"])) {
        $json["error_msg"] =  "Token is expired";
    } else {
        $config = require_once('config.php');
        $db = new DB($config['dsn'], $config['username'], $config['password'], $config['options']);
        $users = $db->search('User', 'user', 'longtitude >= 0 AND latitude >= 0', array());
        $listusers = array();
        foreach ($users as $user) {
            $user = $user->simply();
            $listusers[] = $user;
        }
        $json = array(
            'error' => false,
            'users' => $listusers
        );
    }
}
echo json_encode($json);
