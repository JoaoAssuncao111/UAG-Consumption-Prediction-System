import React from 'react';
import {
  LineChart,
  Line,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer,
  BarChart,
  Bar,
} from 'recharts';

export function TemperatureChart({ data }) {
  return (
    <div style={{ width: '100%' }}>
      <ResponsiveContainer width="100%" height={400}>
        <BarChart data={data} margin={{ left: 20, right: 20 }}>
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis dataKey="dateHour" tickLine={null} />
          <YAxis />
          <Tooltip />
          <Legend />
          <Bar
            type="monotone"
            dataKey="minValue"
            stroke="#0000FF"
            fill="#00b0f3"
            name="Min Temperature"
          />
          <Bar
            type="monotone"
            dataKey="maxValue"
            stroke="#FF0000"
            fill="#ff5d53"
            name="Max Temperature"
          />
        </BarChart>
      </ResponsiveContainer>
    </div>
  );
}

export default TemperatureChart;
