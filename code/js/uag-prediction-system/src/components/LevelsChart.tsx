import React from 'react';
import { AreaChart, XAxis, YAxis, CartesianGrid, Tooltip, Legend, Area, ResponsiveContainer, BarChart, Bar } from 'recharts';
import { format } from "date-fns";

export function LevelsChart({ levels, deliveries }) {

  const mergedData = levels.map((levelItem) => {
    const correspondingDelivery = deliveries.find(
      (deliveryItem) => deliveryItem.date === levelItem.date
    );
      
    return {
      ...levelItem,
      delivery: correspondingDelivery ? correspondingDelivery.load : null,
    };
    

  });
  return (
    
    <div style={{ width: '90%' }}>
      <ResponsiveContainer width="100%" height={720}>
        <BarChart data={mergedData}>
          <XAxis
            dataKey="date"
            tickLine={null}
          />

          <YAxis
            domain={[0, 100]}
          />

          <CartesianGrid opacity={0.1} vertical={false} />

          <Tooltip
            labelFormatter={(value) => {
              const date = new Date(value);
              return format(date, 'MMM d, yyyy, hh:mm');
            }}
          />

          <Legend />

          <Bar
            name="Nivel"
            dataKey="gasLevel"
            stroke="#2451B7"
            fill="#2c7ef2"
          />

          <Bar
            name="Entrega"
            dataKey="delivery"
            stroke="#7ED957"
            fill="#7ED957"
          />
        </BarChart>
      </ResponsiveContainer>
    </div>
  );
}
