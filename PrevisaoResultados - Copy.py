#!/usr/bin/env python
# coding: utf-8

# In[87]:


from sklearn import neural_network
import numpy as np 
import pandas as pd
import matplotlib.pyplot as plt
#import tensorflow as tf
from sklearn.linear_model import LinearRegression
from sklearn.metrics import mean_absolute_error , mean_squared_error 
import warnings
import pickle
from datetime import datetime, timedelta

def MediaMovel(df, tmin, tmax):
    MA= []
    for i in range(len(df)):
        if i >= abs(tmin):
            media = df.iloc[i+tmin:i+tmax+1]['Consumo'].mean()
        else:
            media = None
        MA.append(media)
        
    return MA

def DayType(df):
    lista = [0] * len(df)
    feriados= ["2023-01-01", "2023-04-25", "2023-05-01", "2023-06-10", "2023-08-15", "2023-10-05", "2023-11-01", "2023-12-01", "2023-12-08", "2023-12-25"]
    k = 0
    for i in range(df.index[0],df.index[-1]):
        data_str = df.loc[i, 'Data'].strftime("%Y-%m-%d")
        data = datetime.strptime(data_str, "%Y-%m-%d")
        for j in feriados:
            feriado = datetime.strptime(j, "%Y-%m-%d")
            if data.month == feriado.month and data.day == feriado.day:
                lista[k] = 1; break
                
        if data.weekday() == 5 or data.weekday() == 6:
            lista[k] = 1
        k+=1
        
    return lista


# In[88]:


pd.set_option('display.max_rows', None)  # Show all rows
pd.set_option('display.max_columns', None)  # Show all columns
# Aqui deve ser chamado o json de consumos no período temporal de treino, na localidade indicada
df_origin = pd.read_excel("Teste_Fev_Abril_22_2.xlsx", sheet_name= "Bragança")
#Esta chamada deve ser substituída pelo json de temperaturas no período temporal de treino, na localidade indicada
df_temp= df_origin[["Data", "Tmin","Tmax"]]
df = df_origin[["Data", "Consumo"]]
#df['MA_3a5'] = MediaMovel(df, -5, -3)
#df['MA_3a9'] = MediaMovel(df, -9, -3)
#df['DayType'] = DayType(df)
#df


# In[89]:


df['MA_3a5'] = MediaMovel(df, -5, -3)
df['MA_3a9'] = MediaMovel(df, -9, -3)
df['DayType'] = DayType(df)
#df['Peso_mensal']=df_origin['Peso_mensal']
df['Tmin']=df_temp['Tmin']
df['Tmax']=df_temp['Tmax']
df


# In[90]:


df=df.dropna()
df


# In[91]:


corr = df.corr()
corr.style.background_gradient(cmap='coolwarm')


# In[92]:


X=df.iloc[:50,3:]
Y=df.iloc[:50,1]
X.head
Y.head


# In[93]:


X_test=df.iloc[50:,3:]
Y_test=df.iloc[50:,1]
X_test


# In[94]:


reg = LinearRegression().fit(X, Y)
reg.score(X, Y)


# In[95]:


reg


# In[96]:


reg.coef_,reg.intercept_


# In[97]:


np.mean(abs(reg.predict(X)-Y))


# In[98]:


np.mean(abs(reg.predict(X_test)-Y_test))


# In[99]:


plt.plot(np.arange(len(Y_test)),Y_test)
plt.plot(np.arange(len(Y_test)),reg.predict(X_test))


# # Previsão

# Para prever o consumo nos dias [d_0:d_n], temos que ter os dados da previsão de temperatura e os consumos reais nos dias [d_-9:d_-1]
# Com os consumos passados calculamos as médias móveis e já com estas calculamos as previsões de consumo, que por sua vez alimentarão as médias móveis seguintes.
# 
# Média móvel -9,-3 ----> notação mm
# Consumo --------> notação c
# 
# Usando as datas, temos mm0=avg(C-9:c-3)  c0=reg(mm0,temperaturas, dia, mês), acrescentando-se os valores mm0 e c0 na dataframe  

# # Exemplo

# In[100]:


df_prev=df.iloc[-10:-3]
df_prev


# In[101]:


cpr=df.iloc[-19:-10,:2] # últimos consumos reais (consumos passado recente)
cpr


# ## ➔ Dados de previsão a 7 dias

# In[102]:


size=9+7
df=df[:size]
df.reset_index(drop=True, inplace=True)
df


# In[111]:


for i in range(size):
    df.at[i, 'Tmin'] = None
    df.at[i, 'Tmax'] = None 
    df.at[i, 'Consumo'] = None
    df.at[i, 'MA_3a5'] = None
    df.at[i, 'MA_3a9'] = None
    #df.at[i, 'Data']= datas do conjunto de dias a usar no size
    df.at[i, 'DayType'] = DayType(df)[i]
df


# In[164]:


#consumo_old=np.array([-1,-2,-3,-4,-5,-6,-7,-8,-9.0])

consumo_old=np.array([-10,-11,-12,-10,-11,-12,-10,-11,-12.0]) # Substituir pelo vetor correto


# In[165]:


for i in range(9):
   df.at[i,'Consumo']=consumo_old[i] #Substituir pelo vetor correto
for i in range(9,len(df)):
    df.at[i,'Tmin']=df_temp.at[i,'Tmin'] #Substituir pelo vetor correto
    df.at[i,'Tmax']=df_temp.at[i,'Tmax'] #Substituir pelo vetor correto
df


# In[60]:


coefs=np.array([0.19049154, 1.57162265, 0.02758893, 0.52786252])
print(np.shape(coefs))
intercept=-20.60712828239192


# In[167]:


for i in range(9,9+7): #substituir pelo frame futuro correto  
    df.at[i,'MA_3a9']=MediaMovel(df,-9,-3)[i]
    df.at[i,'Consumo']=intercept+np.dot(coefs,df.iloc[i,3:])
df


# In[169]:


previsão=df['Consumo'][9:]
previsão


# In[ ]:




