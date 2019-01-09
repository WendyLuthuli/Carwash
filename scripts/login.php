<?php
	require "connection.php";
	$email = $_POST["email"];
	$password = $_POST["password"];

	$sql = "SELECT * FROM user WHERE Email =:email AND Password =:password";
	$query = $conn->prepare($sql);
	$query->bindParam(':email',$email);
	$query->bindParam(':password',$password);
	$query->execute();
	$result = $query->fetch(PDO::FETCH_ASSOC);

	$response = array();
	if(!$result)
	{
	$code = "login_failed";
	$message = "Incorrect Email or Password. Please Try Again!";
	array_push($response, array("code"=>$code, "message"=>$message));
    echo json_encode($response);
	}
	else
	{
		$sql = "SELECT * FROM user WHERE Email =:email AND Password=:password";
		$query = $conn->prepare($sql);
		$query->bindParam(':email', $email);
		$query->bindParam(':password', $password);
		$query->execute(array(':email'=>$email, ':password'=>$password));
		$result = $query->fetch(PDO::FETCH_ASSOC);	

		
		if(!$result){
			$code = "login_failed";
			$message = "Incorrect Email Address or Password. Try Again!";
			array_push($response, array("code"=>$code, "message"=>$message));
		    echo json_encode($response);
		}
		elseif ($result) 
		{
			$user_id = $result['UserID'];
			$name = $result['FirstName']." ".$result['LastName'];
			$code = "login_success";
		    $message = "Login Successful";
		    array_push($response, array("code"=>$code, 
		    							"message"=>$message, 
		    							"user_id"=>$user_id,
		    							"name"=>$user_name));
		    echo json_encode($response);
		}
	}

?>