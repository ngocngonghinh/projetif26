<?php

require_once('database/db.php');
require_once('model/user.php');

$parameters = array
    (
    ':email' => null,
    ':password' => null
);
foreach ($_GET as $key => $value) {
    $parameters[":$key"] = $value;
}

$json = array(
    'error' => true
);

$config = require_once('config.php');
$db = new DB($config['dsn'], $config['username'], $config['password'], $config['options']);

/**
 * Decrypting password
 * @param salt, password
 * returns hash string
 */
function checkhashSSHA($salt, $password) {

    $hash = hash('sha256', hash('sha256', $password . $salt) . $salt);

    return $hash;
}

/**
 * Get user by email and password
 */
$user = $db->find('User', 'user', 'email = :email', array(':email' => $parameters[":email"]));
//var_dump($user);
if ($user !== false) {
    $current_date=date('Y-m-d H:i:s');
    if($user->getNext_attempt()<$current_date){
    $hash = checkhashSSHA($user->getSalt(), $parameters[":password"]);
    // check for password equality
    if (strcmp($user->getPassword(),$hash)==0) {
        $token = md5(time() . $user->getEmail() . $user->getPassword());
        $user->setToken($token);
        $user->setLongtitude(-1);
        $user->setLatitude(-1);
        $user->setNbr_failure(0);
        $user->setNext_attempt($current_date);
        if ($db->update($user, 'user', 'id = :id', array(':id' => $user->getId(), ':token' => $token))) {
            $json = array(
                'error' => false,
                'token' => $token
            );
        }
    }
    else{
        $user->setNbr_failure($user->getNbr_failure()+1);
        if ($user->getNbr_failure()>=5){
        $user->setNext_attempt(date('Y-m-d H:i:s', time()+($user->getNbr_failure()-5)*300));
        } 
        $db->update($user, 'user', 'id = :id', array(':id' => $user->getId())); 
    }    
    
}
else{
    echo strtotime($user->getNext_attempt()) - time(). "<br/>";
    $difference = strtotime($user->getNext_attempt()) - time(). "<br/>";
    $json["error_msg"] =  "Authentification is refused, try in ". $difference." s";
}
}

// echo json_encode($json, JSON_PRETTY_PRINT);            5.4 required!!
echo json_encode($json);
