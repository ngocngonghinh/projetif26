<?php
class User
{
	public $id;
	public $name;
	public $email;
	public $password;
	public $token;
        public $longtitude;
        public $latitude;
	public function toDB()
	{
		$object = get_object_vars($this);
		return $object;
	}
}