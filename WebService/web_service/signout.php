<?php

require_once('../database/db.php');
require_once('../model/user.php');
require_once('../control/valideToken.php');

$user = getUserFromToken();

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
