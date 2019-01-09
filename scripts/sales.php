<?php
    session_start();
    require "connection.php";

   
    date_default_timezone_set('Africa/Johannesburg');

    $sales_date = date('Y-m-d');
    $sales_time = date("H:i:s");  
    $grand_total = $_POST["grand_total"];
    $user_id = $_POST["user_id"];
   
    $list = $_POST['list'];
    $list = stripcslashes($list);
    $objA = json_decode($list, true);

    if ($user_id != null) 
    {
        $sql = "INSERT INTO sale(SalesDate, SalesTime, Total, UserID) 
                VALUES(:sales_date, :sales_time, :grand_total, :user_id)";
        $stmt = $conn->prepare($sql);
        $stmt->bindParam(':sales_date', $sales_date);
        $stmt->bindParam(':sales_time', $sales_time);
        $stmt->bindParam(':grand_total', $grand_total);
        $stmt->bindParam(':user_id', $user_id);
        $stmt->execute();

        if($stmt)
        {
            $sql1 ="SELECT SaleID FROM sale 
                    WHERE SalesDate =:sales_date 
                    AND SalesTime =:sales_time 
                    AND Total =:grand_total 
                    AND UserID =:user_id";

            $stmt1 = $conn->prepare($sql1); 
            $stmt1->bindParam(':sales_date', $sales_date);
            $stmt1->bindParam(':sales_time', $sales_time);
            $stmt1->bindParam(':grand_total', $grand_total);
            $stmt1->bindParam(':user_id', $user_id);
            $stmt1->execute();
            $result = $stmt1->fetch();
            $stmt1->closeCursor();
            $arrCol = array();
            $response = array();
       
            $sales_id = $result["SaleID"];
           
        }
        
        if($sales_id != null && $objA != null)
        {
            foreach ($objA as $row)
            {
                $sql4 ="INSERT INTO sale_detail(SaleID, ProductID, Quantity) 
                    VALUES(:sales_id, :product_id, :quantity)";

                $stmt4 = $conn->prepare($sql4); 
                $stmt4->bindParam(':sales_id', $sales_id);
                $stmt4->bindParam(':product_id', $row['product_id']);
                $stmt4->bindParam(':quantity', $row['quantity']);
                $stmt4->execute();
                $stmt4->closeCursor();
        
                foreach ($objA as $row)
                {
                    $sql3 ="UPDATE product 
                        SET Quantity = Quantity - :quantity 
                        WHERE ProductID = :product_id";

                    $stmt3 = $conn->prepare($sql3);
                    $stmt3->bindParam(':product_id', $product_id);
                    $stmt3->bindParam(':quantity', $quantity);
                    $stmt3->execute();
                    $stmt3->closeCursor();
                }
            }
               
            $code = "sales_success";
            $message = "Sales Successfully";
            array_push($response, array("code"=>$code, "message"=>$message));
            //echo json_encode($response);
            
        }
        else
        {

            $code = "sales_failed";
            $message = "Sales Unsuccessfully";
            array_push($response, array("code"=>$code, "message"=>$message));
            
       
        }

       
       
    }
    else
        {

            $code = "sales_failed";
            $message = "Sales Unsuccessfully";
            array_push($response, array("code"=>$code, "message"=>$message));
            
       
        }

    
  
 echo json_encode($response);
    
        
    
    
       
    
    
?>