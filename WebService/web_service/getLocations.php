<?php

require_once('../control/valideToken.php');
$user = getUserFromToken();
if ($user !== false) {
    $config = require_once('config.php');
    $users = $db->search('User', 'user', 'longtitude >= 0 AND latitude >= 0', array());
    $listusers = array();
    foreach ($users as $user) {
        if ($_GET["token"] != $user->getToken()) {
            $user = $user->simply();
            $listusers[] = $user;
        }
    }
    $json = array(
        'error' => false,
        'users' => $listusers
    );
}
echo json_encode($json);
