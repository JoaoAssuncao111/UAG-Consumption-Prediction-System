import React from 'react';
import { AreaChart, XAxis, YAxis, CartesianGrid, Tooltip, Legend, Area, ResponsiveContainer, BarChart, Bar } from 'recharts';
import { format } from "date-fns";

export function LevelsChart({ data }) {
  return (
    <div style={{ width: '90%' }}>
      <ResponsiveContainer width="100%" height={720}>
        <BarChart data={data}>
          <XAxis
            label={{ value: "Data" }}
            dataKey={"date"}
            tickLine={null}
          />

          <YAxis
            label={{ value: 'Consumo', angle: -90, position: 'insideLeft' }}
            domain={['dataMin', 100]}

          />

          <CartesianGrid opacity={0.1} vertical={false} />

          <Tooltip
            labelFormatter={(value) => {
              const date = new Date(value);
              return format(date, "MMM d, yyyy, hh:mm");
            }}
            
          />

          <Legend />

          <Bar
            name="Consumo"
            type="monotone"
            dataKey="consumption"
            stroke="#FF0000"
            fill="#FF0021"
          />

          <Bar
            name="Niveis"
            type="monotone"
            dataKey="gasLevel"
            stroke="#2451B7"
            fill="#2c7ef2"
          />

        </BarChart>
      </ResponsiveContainer>
    </div>
  );
};
