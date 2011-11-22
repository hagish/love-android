
function dump(prefix, x)
	for k,v in pairs(x) do
		if type(v) == "table" then
			dump(prefix .. "." .. k, v)
		else
			print(prefix .. "." .. k, v)
		end
	end 
end

dump("love", love)

os.exit(1)
