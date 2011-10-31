print("test1",2,"test3")

function love.load()
	--~ print("love.load")
	gMyImg = love.graphics.newImage("ship01.png")
end

function love.update(dt)
	--~ print("love.update", dt) 
end

function love.draw()
	--~ print("love.draw")
    --~ love.graphics.print("Hello World", 20, 20)
	love.graphics.draw(gMyImg, 20, 20)
end
