<?php

require_once('../model/friend.php');
require_once('../model/user.php');
require_once('../control/valideToken.php');

$user = getUserFromToken();

if ($user !== false) {
    $friend = new Friend();
    $friend->update($parameters[":id"], $user->getId(), 1);
    $db->update($friend, 'friends', 'id_user1 = :id_user1 AND id_user2 = :id_user2', array());
    $json = array(
        'error' => false,
        'friends' => $friend->toDB()
    );
}
echo json_encode($json);


