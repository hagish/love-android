-------------------------------------------------
-- LOVE: Passing Clouds Demo								
-- Website: http://love.sourceforge.net			
-- Licence: ZLIB/libpng									
-- Copyright (c) 2006-2009 LOVE Development Team
-------------------------------------------------

function love.load()
	
	-- The amazing music.
	music = love.audio.newSource("prondisk.xm")
	
	-- The various images used.
	body = love.graphics.newImage("body.png")
	ear = love.graphics.newImage("ear.png")
	face = love.graphics.newImage("face.png")
	logo = love.graphics.newImage("love.png")
	cloud = love.graphics.newImage("cloud_plain.png")

	-- Set the background color to soothing pink.
	love.graphics.setBackgroundColor(0xff, 0xf1, 0xf7)
	
	-- Spawn some clouds.
	for i=1,5 do
		spawn_cloud(math.random(-100, 900), math.random(-100, 700), 80 + math.random(0, 50))
	end
	
	love.graphics.setColor(255, 255, 255, 200)
	
	love.audio.play(music, 0)
	
	-- love-android font test 
	fontimg = love.graphics.newImage("imgfont.png")
	fontimg:setFilter("nearest","nearest")
	imgfont = love.graphics.newImageFont(fontimg," abcdefghijklmnopqrstuvwxyz0123456789.!'-:·")
	imgfont:setLineHeight(2)
	love.graphics.setFont(imgfont)
end

function love.update(dt)
	love_android_test_update(dt)
	try_spawn_cloud(dt)
	
	nekochan:update(dt)
	
	-- Update clouds.
	for k, c in ipairs(clouds) do
		c.x = c.x + c.s * dt
	end
	
end

-- ***** ***** ***** ***** ***** start love-android test stuff 
--~ imgTerrain = love.graphics.newImage("gfx/terrain.png")
--~ imgTerrain = love.graphics.newImage("test512.png")
--~ imgTerrain = love.graphics.newImage("terrain3.png")
--~ imgTerrain = love.graphics.newImage("body.png")
--~ imgTerrain:setFilter("nearest","nearest")
--~ back_terrain = love.graphics.newQuad(0,112,300,94,512,512)
--~ front_terrain = love.graphics.newQuad(0,224,300,94,512,512)
--~ track_quad = love.graphics.newQuad(0,48,121,5,128,128)
front_x = 0
back_x = 0
WIDTH = 300*3
HEIGHT = 300

function updateTerrain(dt)
	--~ front_x = (front_x + 65*dt) % WIDTH
	--~ back_x = (back_x + 40*dt) % WIDTH
end

function drawTerrain()
	--~ love.graphics.drawq(imgTerrain,back_terrain,0-back_x,0)
	--~ love.graphics.drawq(imgTerrain,back_terrain,WIDTH-back_x,0)
	--~ love.graphics.drawq(imgTerrain,front_terrain,0-front_x,0)
	--~ love.graphics.drawq(imgTerrain,front_terrain,WIDTH-front_x,0)
	--~ love.graphics.draw(body,0,100)
	--~ love.graphics.draw(imgTerrain,512,0)
	--~ love.graphics.draw(imgTerrain,512,512)
	--~ love.graphics.draw(imgTerrain,128,0)
	--~ love.graphics.draw(imgTerrain,128,128)
	--~ love.graphics.draw(imgTerrain,0,0)
	--~ love.graphics.draw(ear,0,0)
	--~ love.graphics.draw(ear,128,128)
	--~ love.graphics.draw(ear,256,256)
	--~ love.graphics.draw(ear,512,512)
	--~ love.graphics.draw(body,0,100)
end

function love_android_test_update (dt)
	--~ updateTerrain(dt)
end
function love_android_test_draw ()
	--~ drawTerrain()
end
function love_android_test_disable_orig () return 2 == 1 end

-- ***** ***** ***** ***** ***** end love-android test stuff 

function love.draw()
	love.graphics.print("hallo welt!",10,10)
	love_android_test_draw()
	if (love_android_test_disable_orig()) then return end


	--~ if (not gMyQuad) then
		--~ local x, y = 0,0
		--~ local width, height = 64,64
		--~ local sw, sh  = 128,128
		--~ gMyQuad = love.graphics.newQuad( x, y, width, height, sw, sh )
	--~ end
	
	--~ love.graphics.drawq( image, quad, x, y, r, sx, sy, ox, oy )
	--~ love.graphics.drawq( body, gMyQuad, 150, 150)
	
	--~ love.graphics.draw(logo, 400, 380, 0, 1, 1, 128, 64)
	local s = 1
	--~ love.graphics.draw(body, 100,100*1, 0, s, s, 0, 0)
	--~ love.graphics.draw(face, 100,100*2, 0, s, s, 0, 0)
	--~ love.graphics.draw(ear, 100,100*3, 0, s, s, 0, 0)
	--~ love.graphics.draw(logo, 100,100*4, 0, s, s, 0, 0)
	
	--~ love.graphics.draw(ear, 100,100*1, 0.1f, s, s, 0, 0)
	--~ love.graphics.draw(ear, 100,100*2, 0.1f, s, s, 16, 16)
	--~ love.graphics.draw(ear, 100,100*3, 0.1f, s, s, 16, -16)
	--~ love.graphics.draw(ear, 200,100*4, 0.1f, s, s, 16, 64+10*1)
	
	--~ love.graphics.draw(ear, 100,100*5, -0.1f, s, s, 0, 0)
	--~ love.graphics.draw(ear, 100,100*6, -0.1f, s, s, 16, 16)
	--~ love.graphics.draw(ear, 100,100*7, -0.1f, s, s, 16, -16)
	--~ love.graphics.draw(ear, 200,100*8, -0.1f, s, s, 16, 64+10*1)
	
	
	love.graphics.draw(logo, 400, 380, 0, 1, 1, 128, 64)
	
	for k, c in ipairs(clouds) do
		love.graphics.draw(cloud, c.x, c.y)
	end
	
	nekochan:render()
	
end

function love.keypressed(k)
	if k == "r" then
		love.filesystem.load("main.lua")()
	end
	if k == "5" then
		os.exit(0)
	end
end


nekochan = {
	x = 400, 
	y = 250, 
	a = 0
}

function nekochan:update(dt)
		self.a = self.a + 10 * dt	
end

function nekochan:render()


	love.graphics.draw(body, self.x, self.y, 0, 1, 1, 64, 64)
	love.graphics.draw(face, self.x, self.y + math.sin(self.a/5) * 3, 0, 1, 1, 64, 64)
	local r = 1 + math.sin(self.a*math.pi/20)
	for i = 1,10 do
		love.graphics.draw(ear, self.x, self.y, (i * math.pi*2/10) + self.a/10, 1, 1, 16, 64+10*r)
	end
	
end

-- Holds the passing clouds.
clouds = {}

cloud_buffer = 0
cloud_interval = 1

-- Inserts a new cloud.
function try_spawn_cloud(dt)

	cloud_buffer = cloud_buffer + dt
	
	if cloud_buffer > cloud_interval then
		cloud_buffer = 0
		spawn_cloud(-512, math.random(-50, 500), 80 + math.random(0, 50))
	end
		
end

function spawn_cloud(xpos, ypos, speed)
	table.insert(clouds, { x = xpos, y = ypos, s = speed } )
end
