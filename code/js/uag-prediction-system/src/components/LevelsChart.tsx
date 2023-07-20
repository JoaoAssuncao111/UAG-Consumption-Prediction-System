import React from 'react';
import { ReferenceLine, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer, BarChart, Bar } from 'recharts';
import { format } from 'date-fns';

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

  
  const renderTooltipLabel = (value) => {
    const date = new Date(value);
    return format(date, 'MMM d, yyyy, hh:mm');
  };

  const customTooltipFormatter = (value, name, entry) => {
    const date = new Date(entry.payload.date);
    const formattedDate = format(date, 'MMM d, yyyy, hh:mm');
    const gasLevel = entry.dataKey === 'gasLevel' ? value : null;
    const delivery = entry.dataKey === 'delivery' ? value : null;

 
    const predictionType =
      entry.dataKey === 'gasLevel' ? levels.find((levelItem) => levelItem.date === entry.payload.date)?.predictionType : null;

   

    return (
      <div className="custom-tooltip">

        {gasLevel && <p>{`Nível de Depósito: ${gasLevel}`}</p>}
        {delivery && <p>{`Carga: ${delivery}`}</p>}
        {predictionType && entry.dataKey === 'gasLevel' && <p>{`Valor de Previsão: ${predictionType}`}</p>}
      
      </div>
    );
  };


  const CustomTooltipLabel = ({ active, payload }) => {
    if (active && payload && payload.length) {
      const date = new Date(payload[0].payload.date);
      const formattedDate = format(date, 'MMM d, yyyy, hh:mm');
      return <p>Data: {formattedDate}</p>;
    }
    return null;
  };

  return (
    <div style={{ width: '90%' }}>
      <ResponsiveContainer width="100%" height={720}>
        <BarChart data={mergedData}>
          <XAxis dataKey="date" tickLine={null} />
          <YAxis domain={[0, 100]} />
          <CartesianGrid opacity={0.1} vertical={false} />

       
          <Tooltip
            label={CustomTooltipLabel}
            formatter={customTooltipFormatter}
            
          />

          <Legend />

        
          <Bar name="Nivel" dataKey="gasLevel" stroke="#2451B7" fill="#2c7ef2" />
          <Bar name="Entrega" dataKey="delivery" stroke="#7ED957" fill="#7ED957" />

          <ReferenceLine y={35} stroke="#FF0000" strokeWidth={4} />
        </BarChart>
      </ResponsiveContainer>
    </div>
  );
}
