import * as React from "react";
import { Link } from "react-router-dom";
import { useState,useEffect } from "react";
import { api } from "../api";

export function Home() {

  
  
  return (
    <div>
    <h1>Home</h1>
    <Link to="/temperature">Data Representation</Link>
    <Link to="/uags">Todas as UAGS</Link>
  </div>
  );
}