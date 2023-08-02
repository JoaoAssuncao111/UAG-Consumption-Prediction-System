import React from 'react';
import { ReferenceLine, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer, BarChart, Bar, Cell } from 'recharts';
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

  const realData = mergedData.filter((entry) => entry.predictionId === 0);
  const predictionData = mergedData.filter((entry) => entry.predictionId !== 0);

  const customTooltipFormatter = (value, name, entry) => {
    const date = new Date(entry.payload.date);
    const formattedDate = format(date, 'MMM d, yyyy, hh:mm');
    const gasLevel = entry.dataKey === 'gasLevel' ? value : null;
    const delivery = entry.dataKey === 'delivery' ? value : null;

    const predictionType =
      entry.dataKey === 'gasLevel'
        ? levels.find((levelItem) => levelItem.date === entry.payload.date)?.predictionType
        : null;

    return (
      <div className="custom-tooltip" style={{ color: entry.fill }}>
        {gasLevel && <p>{`Nível de Depósito: ${gasLevel}`}</p>}
        {delivery && <p>{`Carga: ${delivery}`}</p>}
        {predictionType && entry.dataKey === 'gasLevel' && (
          <p>{`Valor de Previsão: ${predictionType}`}</p>
        )}
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

  // Function to determine the fill color based on the dataset
  const barFill = (entry) => {
    return entry.predictionId === 0 ? '#2c7ef2' : '#FFA500';
  };

  // Custom legend payload with correct colors
  const legendPayload = [
    {
      value: 'Nivel',
      type: 'square',
      id: 'gasLevel',
      color: '#2c7ef2',
    },
    {
      value: 'Nivel (Prediction)',
      type: 'square',
      id: 'gasLevelPrediction',
      color: '#FFA500',
    },
    {
      value: 'Entrega',
      type: 'square',
      id: 'delivery',
      color: '#7ED957',
    },
  ];

  return (
    <div style={{ width: '90%' }}>
      <ResponsiveContainer width="100%" height={720}>
        <BarChart data={[...realData, ...predictionData]}>
          <XAxis dataKey="date" tickLine={null} />
          <YAxis domain={[0, 100]} />
          <CartesianGrid opacity={0.1} vertical={false} />

          <Tooltip
            label={CustomTooltipLabel}
            formatter={customTooltipFormatter}
          />

          {/* Render bars with customized fill colors */}
          <Bar name="Nivel" dataKey="gasLevel" stroke="#2451B7">
            {mergedData.map((entry, index) => (
              <Cell key={`cell-${index}`} fill={barFill(entry)} />
            ))}
          </Bar>
          <Bar name="Entrega" dataKey="delivery" stroke="#7ED957" fill="#7ED957" />

          <ReferenceLine y={35} stroke="#FF0000" strokeWidth={4} />

          {/* Use the custom legend payload */}
          <Legend payload={legendPayload as any} />
        </BarChart>
      </ResponsiveContainer>
    </div>
  );
}
