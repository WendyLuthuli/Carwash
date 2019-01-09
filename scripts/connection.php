<?php

	$host='154.0.170.55';
	$database='zhaafxuh_carwash';
	$username = 'zhaaf_carwash';
	$password= 'F@wendy12345';
	
	//PDO object and PDO execption
	try {
	$conn = new PDO("mysql:host=$host;dbname=$database", $username, $password);
	$conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

	#echo '<p> You are connected to the db';
	}
	catch (PDOException $e){
		$error_message = $e->getMessage();
		echo "<p> And errror occured:$error_message</p>";
	}
	
	
?>