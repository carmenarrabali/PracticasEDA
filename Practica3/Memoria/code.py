import matplotlib.pyplot as plt

x = int(input('Ingrese numero hasta el cual iterar: '))

for n in range(2,x):
    z = 0
    g = n
    while n!=1:
        if n%2==0:
            n = n/2
        else:
            n = 3*n+1  
        z=z+1
    plt.plot(g,z, marker='o', markersize=1, color='m')

plt.figure(dpi=500)
plt.title('Grafico Numero vs. Iteracion')
plt.xlabel('Numero inicial')
plt.ylabel('Cantidad de iteraciones')
plt.savefig('collatz1.png')

