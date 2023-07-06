import * as React from "react";
import { Link } from "react-router-dom";
import { useState,useEffect } from "react";
import { Header } from "./Header";

export function ReadingChoice(){
    return(
        <div>
            <Header></Header>
        <div className="center-items">
            <Link className="link"to="/temperature">Temperaturas</Link>
            <Link className="link"to="/humidity">Humidade</Link>
            <Link className="link"to="/levels">Consumos</Link>
            <Link className="link"to="/uags">Todas as UAGS</Link>
        </div>
        </div>
    )
}