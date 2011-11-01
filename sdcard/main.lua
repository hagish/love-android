
-- love.filesystem.load("lala.lua")()

function love.load()
	--~ print("love.load")
	gMyImg = love.graphics.newImage("ship03.png") -- ship01.png=200x200,  ship02.png=256x256 ship03.png=256x256-with-test-pattern
	-- lala()
end

function love.keypressed(key, unicode)
	print("keypressed", key, unicode)
	toast("keypressed", key, unicode)	
end

function love.keyreleased(key)
	print("keyreleased", key)
end

function love.mousepressed(x, y, button)
	print("mousepressed", x, y, button)
end

function love.mousereleased(x, y, button)
	print("mousereleased", x, y, button)
end

function love.update(dt)
	--~ print("love.update", dt) 
	--~ print("mouse pos", love.mouse.getPosition()) 
	--~ print("mouse button", love.mouse.isDown("l"), love.mouse.isDown("r")) 
	--~ print("fps", love.timer.getFPS( ), love.timer.getTime( ))
end

function love.draw()
	--~ print("love.draw")
    --~ love.graphics.print("Hello World", 20, 20)
	local t = love.timer.getTime()
	--~ print("curtime",t)
	local s = 1/2
	local sd = 2
	local od = 128,128
	--~ local od = 0,0
	local sx,sy = 2*s,1*s
	local ox,oy = od,od
	love.graphics.draw(gMyImg, 200, 200, t*4, sx,sy, ox,oy)
end
