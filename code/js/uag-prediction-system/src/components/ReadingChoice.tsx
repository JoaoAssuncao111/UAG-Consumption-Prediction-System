import * as React from "react";
import { Link } from "react-router-dom";
import { useState,useEffect } from "react";
import { api } from "../api";

export function ReadingChoice(){
    return(
        <div>
            <Link to="/temperature">Temperature</Link>
            <Link to="/humidity">Humidity</Link>
            <Link to="/levels">Consumption</Link>
        </div>
    )
}