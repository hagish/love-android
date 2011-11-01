print("test1",2,"test3")

function love.load()
	--~ print("love.load")
	gMyImg = love.graphics.newImage("ship02.png") -- ship01.png=200x200,  ship02.png=256x256
end

function love.keypressed(key, unicode)
	print("keypressed", key, unicode)
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
	print("curtime",t)
	love.graphics.draw(gMyImg, 20, 20, t)
end
