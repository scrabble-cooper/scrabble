## Change to read from directory later

s="""TW,NM,NM,DL,NM,NM,NM,TW,NM,NM,NM,DL,NM,NM,TW,
NM,DW,NM,NM,NM,TL,NM,NM,NM,TL,NM,NM,NM,DW,NM,
NM,NM,DW,NM,NM,NM,DL,NM,DL,NM,NM,NM,DW,NM,NM,
DL,NM,NM,DW,NM,NM,NM,DL,NM,NM,NM,DW,NM,NM,DL,
NM,NM,NM,NM,DW,NM,NM,NM,NM,NM,DW,NM,NM,NM,NM,
NM,TL,NM,NM,NM,TL,NM,NM,NM,TL,NM,NM,NM,TL,NM,
NM,NM,DL,NM,NM,NM,CDL,NM,DL,NM,NM,NM,DL,NM,NM,
TW,NM,NM,DL,NM,CNM,ANM,TDW,NM,NM,NM,DL,NM,NM,TW,
NM,NM,DL,NM,NM,NM,TDL,NM,DL,NM,NM,NM,DL,NM,NM,
NM,TL,NM,NM,NM,TL,NM,NM,NM,TL,NM,NM,NM,TL,NM,
NM,NM,NM,NM,DW,NM,NM,NM,NM,NM,DW,NM,NM,NM,NM,
DL,NM,NM,DW,NM,NM,NM,DL,NM,NM,NM,DW,NM,NM,DL,
NM,NM,DW,NM,NM,NM,DL,NM,DL,NM,NM,NM,DW,NM,NM,
NM,DW,NM,NM,NM,TL,NM,NM,NM,TL,NM,NM,NM,DW,NM,
TW,NM,NM,DL,NM,NM,NM,TW,NM,NM,NM,DL,NM,NM,TW"""

L = s.split(',\n')
L_final = []
for line in L:
  # line = line.join(("{","},"))
  line = line.split(",")
  # line = '{' + line + '}'
  L_final.append(line)

print("Copy below verbatim:")
print("'"+str(L_final).replace('[','{').replace(']','}\n').replace("\'",'\"')+"'"
