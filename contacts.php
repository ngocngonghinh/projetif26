<?php
require_once('database/db.php');
require_once('model/contact.php');
require_once('model/message.php');
require_once('model/user.php');

$parameters = array
(
	':token' => null
);
foreach($_GET as $key => $value)
{
	$parameters[":$key"] = $value;
}

$json = array(
	'error' => true
);

$config = require_once('config.php');
$db = new DB($config['dsn'], $config['username'], $config['password'], $config['options']);

$user = $db->find('User', 'user', 'token = :token', $parameters);

if($user !== false)
{
	$user->id = (int) $user->id;

	$contacts = $db->search('Contact', 'contact', 'initiator = :id OR contact = :id', array(':id' => $user->id));

	foreach($contacts as $contact)
	{
		if($user->id != $contact->initiator)
		{
			$contact->contact = $db->find('User', 'user', 'id = :id', array(':id' => $contact->initiator));

			unset($contact->contact->password);
			unset($contact->contact->token);
		}
		elseif($user->id != $contact->contact)
		{
			$contact->contact = $db->find('User', 'user', 'id = :id', array(':id' => $contact->contact));
			
			unset($contact->contact->password);
			unset($contact->contact->token);
		}
		$contact->contact->id = (int) $contact->contact->id;
		$contact->id = (int) $contact->id;
		
		unset($contact->contact->id);
		unset($contact->initiator);


		$contact->message = $db->find('Message', 'message', 'contact = :contact', array(':contact' => (int) $contact->id), 'date asc');
		
		if(!$contact->message)
		{
			unset($contact->message);
		}
		else
		{
			$contact->message->id = (int) $contact->message->id;

			unset($contact->message->id);
			unset($contact->message->contact);
			unset($contact->message->user);
		}
	}
	$json = array(
		'error' => false,
		'contacts' => $contacts
	);
}
// echo json_encode($json, JSON_PRETTY_PRINT);            5.4 required!!
echo json_encode($json);