print("test1",2,"test3")

love = {}

function love.load() print("love.load") end
function love.upload(dt) print("love.upload", dt) end
function love.draw() print("love.draw") end

