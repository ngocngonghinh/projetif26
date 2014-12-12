<?php

class Friend {

    //user1 is asker
    private $id_user1;
    private $id_user2;
    private $valide;

    function getId_user1() {
        return $this->id_user1;
    }

    function getId_user2() {
        return $this->id_user2;
    }

    function getValide() {
        return $this->valide;
    }

    function setId_user1($id_user1) {
        $this->id_user1 = $id_user1;
    }

    function setId_user2($id_user2) {
        $this->id_user2 = $id_user2;
    }

    function setValide($valide) {
        $this->valide = $valide;
    }

    public function toDB() {
        $object = get_object_vars($this);
        return $object;
    }

    public function update($id_user1, $id_user2, $valide) {
        $this->id_user1 = $id_user1;
        $this->id_user2 = $id_user2;
        $this->valide = $valide;
    }

}
