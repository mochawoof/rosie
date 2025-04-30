print "Init"

help = "cls ans "
for k, v in pairs(math) do
    _G[k] = v
    help = help .. k .. " "
end