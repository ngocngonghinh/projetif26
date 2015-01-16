<?php

require_once('../database/db.php');
require_once('../control/valideToken.php');

$user = getUserFromToken();

if ($user !== false) {
    $user->setLatitude($parameters[":latitude"]);
    $user->setLongtitude($parameters[":longtitude"]);
    if ($db->update($user, 'user', 'id = :id', array(':id' => $user->getId()))) {
        $json = array(
            'error' => false,
            'user' => $user->simply()
        );
    }
}
echo json_encode($json);

