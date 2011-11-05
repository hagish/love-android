-- called after setting up the java part of the love api

function love.draw() end
function love.focus() end
function love.joystickpressed() end
function love.joystickreleased() end
function love.keypressed() end
function love.keyreleased() end
function love.load() end
function love.mousepressed() end
function love.mousereleased() end
function love.quit() end
function love.run() end
function love.update() end

function require(file)
	if (string.sub(file,-4) ~= ".lua") then file = file .. ".lua" end
	print("love-android require:".."'"..tostring(file).."'")
	return love.filesystem.load(file)()
end

function love_andorid_list_iter (t)
	local i = 0
	local n = table.getn(t)
	return function ()
		i = i + 1
		if i <= n then return t[i] end
	end
end
