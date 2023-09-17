import React from "react";
import { Link } from "react-router-dom";
import { useState, useEffect } from "react";
import { api } from "../../api";
import { Header } from "../utils/Header";

export function Home() {
  return (
    <div>
    <Header></Header>
      <div className="center-items">
        <Link className="link" to="/readings">Leituras de dados</Link>
        <Link className="link" to= "/insertions">Manipulação de dados</Link>
      </div>
    </div>
  );
}