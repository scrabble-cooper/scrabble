s = "A-9, B-2, C-2, D-4, E, F-2, G-3, H-2, I-9, J-1, K-1, L-4, M-2, N-6, O-8, P-2, Q-1, R-6, S-4, T-6, U-4, V-2, W-2, X-1, Y-2, Z-1"
new_s = s.replace('-','').split(', ')
print(new_s)
alpha = ''
for x in new_s:
    if x[0] == 'E':
        alpha += 12*'E' # only letter w double digit count
    else:
        alpha += x[0]*int(x[1])
print (alpha)
print (len(alpha))
