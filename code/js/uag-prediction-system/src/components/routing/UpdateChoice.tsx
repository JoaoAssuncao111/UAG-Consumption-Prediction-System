import * as React from "react";
import { Link } from "react-router-dom";
import { useState, useEffect } from "react";
import { api } from "../../api";
import { Header } from "../utils/Header";

export function UpdateChoice() {
    return (
        <div>
            <Header></Header>
            <div className="center-items">
                <Link className="link" to="/insertuag">Gerir UAGs</Link>
                <Link className="link" to="/predict">Treinar UAG/Prever Consumos</Link>
                <Link className="link" to="/ipma"> Buscar Dados Diários IPMA </Link>
                <Link className="link" to="/update"> Inserir/Atualizar Níveis </Link>
            </div>
        </div>
    )
}