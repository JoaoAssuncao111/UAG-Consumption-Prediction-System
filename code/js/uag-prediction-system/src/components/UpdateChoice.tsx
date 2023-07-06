import * as React from "react";
import { Link } from "react-router-dom";
import { useState, useEffect } from "react";
import { api } from "../api";
import { Header } from "./Header";

export function UpdateChoice() {
    return (
        <div>
            <Header></Header>
            <div className="center-items">
                <Link className="link" to="/insertuag">Inserir UAG</Link>
                <Link className="link" to="/humidity">Treinar UAG</Link>
                <Link className="link" to="/levels">Prever Consumos UAG</Link>
            </div>
        </div>
    )
}