<?php

class User {

    private $id;
    private $name;
    private $email;
    private $salt;
    private $password;
    private $token;
    private $nbr_failure;
    private $next_attempt;
    private $longtitude;
    private $latitude;

    public function toDB() {
        $object = get_object_vars($this);
        return $object;
    }

    function getId() {
        return $this->id;
    }

    function getName() {
        return $this->name;
    }

    function getEmail() {
        return $this->email;
    }

    function getSalt() {
        return $this->salt;
    }

    function getPassword() {
        return $this->password;
    }

    function getToken() {
        return $this->token;
    }

    function getLongtitude() {
        return $this->longtitude;
    }

    function getLatitude() {
        return $this->latitude;
    }

    function setId($id) {
        $this->id = $id;
    }

    function setName($name) {
        $this->name = $name;
    }

    function setEmail($email) {
        $this->email = $email;
    }

    function setSalt($salt) {
        $this->salt = $salt;
    }

    function setPassword($password) {
        $this->password = $password;
    }

    function setToken($token) {
        $this->token = $token;
    }

    function setLongtitude($longtitude) {
        $this->longtitude = $longtitude;
    }

    function setLatitude($latitude) {
        $this->latitude = $latitude;
    }

    function getNbr_failure() {
        return $this->nbr_failure;
    }

    function getNext_attempt() {
        return $this->next_attempt;
    }

    function setNext_attempt($next_attempt) {
        $this->next_attempt = $next_attempt;
    }

    function setNbr_failure($nbr_failure) {
        $this->nbr_failure = $nbr_failure;
    }

    function simply() {
        unset($this->email);
        unset($this->password);
        unset($this->token);
        unset($this->nbr_failure);
        unset($this->attempt);
        $object = get_object_vars($this);
        return $object;
    }

}
