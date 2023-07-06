import React from 'react';
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, AreaChart, ResponsiveContainer, Area } from 'recharts';
import { format } from "date-fns"

export function HumidityChart({ data }) {
  return (
    <div style={{ width: '90%' }}>
      <ResponsiveContainer width="100%" height={720}>
        <AreaChart data={data}>

          <XAxis
            label={{ value: "Data" }}
            dataKey={"dateHour"}
            tickLine={false}
            tickFormatter={string => {
              const date = new Date(string)
              if (date.getDate() % 7 == 0 && date.getHours() === 12) {
                console.log(date)
                return format(date, "MMM,d,yyyy")
              }
              return ""
            }} />

          <YAxis label={{ value: 'Humidade', angle: -90, position: 'insideLeft' }} domain={[0, 100]} />

          <CartesianGrid opacity={0.1} vertical={false} />

          <Tooltip labelFormatter={(value) => {
            const date = new Date(value)
            return format(date, "MMM d, yyyy, hh:mm")
          }} />

          <Legend />

          <Area name="Humidade" type="monotone" dataKey="value" stroke="#2451B7" fill="#2c7ef2" />

        </AreaChart>
      </ResponsiveContainer>

    </div >
  );
};


