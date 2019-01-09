<?php
	require "connection.php";
	$firstname = $_POST["firstname"];
	$lastname = $_POST["lastname"];
	$email = $_POST["email"];
	$password = $_POST["password"];
	$usertype = $_POST["usertype"];
	$subcription = $_POST["subscription"];

	$sql = "INSERT INTO user(FirstName, LastName, Subscription, Email, Password, UserType) VALUES ";
	$query = $conn->prepare($sql);
	$query->bindParam(':firstname', $firstname);
	$query->bindParam(':lastName', $lastName);
	$query->bindParam(':subcription', $subcription);
	$query->bindParam(':email',$email);
	$query->bindParam(':password',$password);
	$query->bindParam(':usertype',$usertype);
	$result = $query->execute();


	$response = array();
	if(!$result)
	{
	$code = "reg_failed";
	$message = "Sign Up Unsuccessful. Please Try Again!";
	array_push($response, array("code"=>$code, "message"=>$message));
    echo json_encode($response);
	}
	else
	{
		$code = "reg_success";
		$message = "Sign Up Successful, You can now sign in!";	
	}
	array_push($response, array("code"=>$code, "message"=>$message));
	echo json_encode($response);
?>