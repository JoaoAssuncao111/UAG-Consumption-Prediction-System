import * as React from "react";
import { Link } from "react-router-dom";
import { useState } from "react";

export function Home(){
  React.useEffect(() => {
        console.log("Home loaded")

  },[])
  
  return (
  <div>
    <div>Test</div>
  </div>
  );
}