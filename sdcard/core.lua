print("test1",2,"test3")

function love.load()
	--~ print("love.load")
	gMyImg = love.graphics.newImage("ship02.png") -- ship01.png=200x200,  ship02.png=256x256
end

function love.update(dt)
	--~ print("love.update", dt) 
end

function love.draw()
	--~ print("love.draw")
    --~ love.graphics.print("Hello World", 20, 20)
	love.graphics.draw(gMyImg, 20, 20)
end
